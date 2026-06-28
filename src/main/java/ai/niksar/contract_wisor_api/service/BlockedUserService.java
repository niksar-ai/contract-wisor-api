package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.model.BlockedUser;
import ai.niksar.contract_wisor_api.repository.BlockedUserRepository;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BlockedUserService {

    @Autowired
    private BlockedUserRepository blockedUserRepository;

    @Transactional
    public void blockUserSave(UUID userId, String reason, LocalDateTime unblockedAt) {
        List<BlockedUser> blockedUsers = blockedUserRepository.findByUserIdAndStatus(userId, Status.ACTIVE);

        if (blockedUsers.isEmpty()) {
            throw new IllegalStateException("User is already blocked!");
        }
        BlockedUser blockedUser = new BlockedUser();
        blockedUser.setUserId(userId);
        blockedUser.setBlockedAt(LocalDateTime.now());
        blockedUser.setUnblockedAt(unblockedAt);
        blockedUser.setStatus(Status.ACTIVE);
        blockedUser.setReason(reason);

        blockedUserRepository.save(blockedUser);
    }

    @Transactional
    public void unblockUserUpdate(UUID userId) {
        List<BlockedUser> blockedUsers = blockedUserRepository.findByUserIdAndStatus(userId, Status.ACTIVE);

        if (blockedUsers.isEmpty()) {
            throw new IllegalStateException("Blocked user not found!");
        }

        for (BlockedUser blockedUser : blockedUsers) {
            blockedUser.setStatus(Status.PASSIVE);
            blockedUserRepository.save(blockedUser);
        }
    }

    public List<BlockedUser> getAllUsersToUnblock(LocalDateTime currentDate) {
        return blockedUserRepository.findAllByStatusAndUnblockedAtBefore(Status.ACTIVE, currentDate);
    }

}