package sagnikverse.IPAM.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sagnikverse.IPAM.network.entity.Network;

import java.util.Optional;

@Repository
public interface NetworkRepository
        extends JpaRepository<Network, Long> {

    Optional<Network> findByCidr(String cidr);
}