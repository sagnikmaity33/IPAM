package sagnikverse.IPAM.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sagnikverse.IPAM.audit.entity.AuditLog;

@Repository
public interface AuditRepository
        extends JpaRepository<AuditLog, Long> {
}