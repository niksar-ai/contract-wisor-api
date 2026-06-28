package ai.niksar.contract_wisor_api.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.*;
import ai.niksar.contract_wisor_api.repository.RoleApiUrlRepository;
import ai.niksar.contract_wisor_api.repository.RoleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleApiUrlService {
    @Autowired
    private ApiUrlService apiUrlService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleApiUrlRepository roleApiUrlRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserService userService;

    @Transactional
    public ResponseDTO saveRoleApiUrl(UUID id, Map<String, List<String>> input){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Role role=roleRepository.findById(id).orElseThrow(ContractWisorException.E008::new);
        if(role.isAdmin() && !userRoleService.isAdminUser(username)){
            throw new ContractWisorException.E028();
        }
        List<String> urlList = input.get("urlList");
        List<RoleApiUrl> apiUrlList=roleApiUrlRepository.findByRoleId(id);
        apiUrlList.forEach(item->roleApiUrlRepository.deleteById(item.getId()));
        urlList.forEach(item->{
            RoleApiUrl roleApiUrl= new RoleApiUrl();
            roleApiUrl.setRole(roleRepository.findById(id).orElseThrow(ContractWisorException.E008::new));
            roleApiUrl.setApiUrl(apiUrlService.getApiUrlById(UUID.fromString(item)));
            roleApiUrlRepository.save(roleApiUrl);
            roleApiUrlRepository.flush();
        });
        ResponseDTO responseDTO=new ResponseDTO();
        responseDTO.setMessage("Role action association operations completed successfully.");
        return responseDTO;
    }
    public void checkRoleApiPermission(String username,String apiUrlPath){
        List <String> urlList= new ArrayList<>();
        User user=userService.findUserByUsername(username);
        List<UserRole> userRoles=userRoleService.getRolesByUserId(user.getId());
        userRoles.forEach(item->{
            List<String> tempUrlList=roleApiUrlRepository.findUrlsByRoleId(item.getRole().getId()).stream()
                    .filter(it -> !urlList.contains(it))
                    .collect(Collectors.toList());
            urlList.addAll(tempUrlList);
        });
        if(urlList.isEmpty() || !urlList.contains(apiUrlPath)){
            throw new ContractWisorException.E028();
        }
    }
    public List<ApiUrl> getApiUrlListWithRoleId(UUID roleId){
        List<ApiUrl> apiUrls=apiUrlService.getList();
        List<UUID> apiUrlList=roleApiUrlRepository.idListByRoleId(roleId);
        apiUrls.forEach(item-> item.setRoleAssign(apiUrlList.contains(item.getId())));
        return apiUrls;
    }
    public void deleteRoleApiUrl(UUID urlId){
        roleApiUrlRepository.deleteByUrlId(urlId);
        roleApiUrlRepository.flush();
    }
    public List<String> actionListByRole(String username){
        User user=userService.findUserByUsername(username);
        List<UUID> roleIdList=userRoleService.roleIdListWithUser(user.getId());
        return roleApiUrlRepository.findActionCodesWithRoleIdList(roleIdList);
    }
    @Transactional
    public void assignAllActionsToAdmin(){
        Role adminRole=roleRepository.findByIsAdminTrue();
        List<RoleApiUrl> oldList=roleApiUrlRepository.findByRoleId(adminRole.getId());
        oldList.forEach(item-> roleApiUrlRepository.deleteById(item.getId()));
        List<ApiUrl> allApiUrlList=apiUrlService.getList();
        allApiUrlList.forEach(it->{
            RoleApiUrl roleApiUrl= new RoleApiUrl();
            roleApiUrl.setRole(adminRole);
            roleApiUrl.setApiUrl(it);
            roleApiUrl.setCreateDate(LocalDateTime.now());
            roleApiUrlRepository.save(roleApiUrl);
            roleApiUrlRepository.flush();
        });
    }

}
