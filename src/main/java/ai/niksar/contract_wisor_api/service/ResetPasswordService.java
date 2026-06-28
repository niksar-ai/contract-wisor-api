package ai.niksar.contract_wisor_api.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.PasswordDTO;
import ai.niksar.contract_wisor_api.dto.ResponseDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.ResetPassword;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.repository.ResetPasswordRepository;
import ai.niksar.contract_wisor_api.util.Constants;
import ai.niksar.contract_wisor_api.util.Util;
import ai.niksar.contract_wisor_api.workflow.DocumentParseWorkflowImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
public class ResetPasswordService {

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Value("${auth.reset.password.expiry}")
    private long resetPasswordExpiry;
    @Value("${company.name}")
    private String companyName;
    @Value("${company.mail.info}")
    private String companyMailInfo;
    @Value("${web.domain}")
    private String webDomain;
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);
    public ResetPassword createSavePassword(String username){
        ResetPassword resetPassword=new ResetPassword();
        resetPassword.setCreateDate(LocalDateTime.now());
        resetPassword.setUsername(username);
        resetPassword.setStatus("1");
        resetPassword.setUpdateDate(LocalDateTime.now());
        resetPassword.setExpiryDate(LocalDateTime.now().plusSeconds(resetPasswordExpiry/1000));
        return resetPasswordRepository.save(resetPassword);
    }

    public ResetPassword getResetPassword(UUID id){
        return resetPasswordRepository.findById(id).orElseThrow(ContractWisorException.E030::new);
    }
    public void controlActiveResetPassword(ResetPassword resetPassword){
        if(!resetPassword.getStatus().equals("1")){
            throw new ContractWisorException.E032();
        }
        if(resetPassword.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new ContractWisorException.E031();
        }
    }
    public void setPassiveStatus(UUID id){
        ResetPassword resetPassword=getResetPassword(id);
        resetPassword.setStatus("0");
        resetPassword.setUpdateDate(LocalDateTime.now());
        resetPasswordRepository.save(resetPassword);
    }
    public ResponseDTO forgotPassword(Map<String,String> input){
        String mail=input.get("email");
        if(!Util.isValidEmail(mail)){
            throw new ContractWisorException.E026();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        User user=userService.findUserByEmail(mail);
        if(user.getStatus().equals(Constants.Status.PASSIVE)){
            throw new ContractWisorException.E011();
        }
        ResetPassword resetPassword=createSavePassword(user.getUsername());
        String body="Hello "+ user.getUsername()+ " ,"
                +"\n Your password reset request has been received. To reset your password, click the link below: \n "+webDomain+"/reset-password/"+resetPassword.getId()
                +"\n This link is valid until "+resetPassword.getExpiryDate().format(formatter)+". If you did not initiate this request, please disregard this email or contact us."
                +"\n Thank you \n The "+companyName+" Team"
                +"\n For support: "+companyMailInfo;

        emailService.sendMail(user.getEmail(),"Your Password Reset Request",body,resetPassword.getId());
        ResponseDTO responseDTO= new ResponseDTO();
        responseDTO.setMessage("A password reset link has been sent to your email address.");
        return responseDTO;
    }

    public ResetPassword getActiveResetPassword(UUID id){
        ResetPassword resetPassword=getResetPassword(id);
        controlActiveResetPassword(resetPassword);
        return resetPassword;
    }
    @Transactional
    public ResponseDTO resetUserPassword(UUID id, PasswordDTO passwordDTO){
        ResetPassword resetPassword=getActiveResetPassword(id);
        ResponseDTO responseDTO= userService.resetUserPasswordWithUsername(resetPassword.getUsername(),passwordDTO);
        setPassiveStatus(id);
        return responseDTO;
    }
    public void deleteResetPass(UUID id){
        resetPasswordRepository.deleteById(id);
    }
}
