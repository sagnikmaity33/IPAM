package sagnikverse.IPAM.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sagnikverse.IPAM.audit.entity.AuditLog;
import sagnikverse.IPAM.audit.repository.AuditRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    public void log(String action,
                    String entityType,
                    String entityId,
                    String details){

        AuditLog log = new AuditLog();

        log.setActionType(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);

        auditRepository.save(log);
    }
}