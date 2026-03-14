package sagnikverse.IPAM.ip.repository;


import sagnikverse.IPAM.ip.entity.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IpRepository extends JpaRepository<IpAddress, Long> {

    Optional<IpAddress> findByIpAddress(String ip);

    List<IpAddress> findBySubnetId(Long subnetId);

    boolean existsByIpAddress(String ip);

}