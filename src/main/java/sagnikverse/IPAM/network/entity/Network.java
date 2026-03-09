package sagnikverse.IPAM.network.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "networks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Network {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cidr;

    private String networkAddress;

    private String broadcastAddress;

    private Long totalIps;

    private LocalDateTime createdAt;
}