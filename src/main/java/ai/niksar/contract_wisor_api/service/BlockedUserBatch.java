package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.batch.BatchService;
import ai.niksar.contract_wisor_api.exception.CriticalBatchException;
import ai.niksar.contract_wisor_api.model.BlockedUser;
import ai.niksar.contract_wisor_api.repository.BlockedUserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BlockedUserBatch implements BatchService {

    @Autowired
    private BlockedUserService blockedUserService;

    @Autowired
    private BlockedUserRepository blockedUserRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void execute() throws CriticalBatchException {
        try {
            List<BlockedUser> blockedUsersToUnblock = blockedUserService.getAllUsersToUnblock(LocalDateTime.now());

            for (BlockedUser blockedUser : blockedUsersToUnblock) {
                userService.unblockUser(blockedUser.getUserId());
                contractWisorLogger.info("Unblocked user ID: {}", blockedUser.getUserId());
            }
        } catch (Exception e) {
            contractWisorLogger.error("Error during the unblock batch operation: ", e);
            throw new CriticalBatchException("Batch execution failed ", e);
        }
    }
}