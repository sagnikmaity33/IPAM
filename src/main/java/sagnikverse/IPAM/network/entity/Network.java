package sagnikverse.IPAM.network.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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