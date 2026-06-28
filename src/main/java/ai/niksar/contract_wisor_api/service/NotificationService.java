package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.Notification;
import ai.niksar.contract_wisor_api.repository.NotificationRepository;

import java.util.UUID;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification){
        return notificationRepository.save(notification);
    }
    public Notification createNotification(Notification notification){
        return saveNotification(notification);
    }
    public Notification getNotification(UUID id){
        return notificationRepository.findById(id).orElseThrow(ContractWisorException.E030::new);
    }
}
