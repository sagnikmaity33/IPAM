package sagnikverse.IPAM.ip.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ip_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long subnetId;

    @Column(unique = true)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private IpStatus status;

    private String hostname;

    private String macAddress;

    private String deviceType;

    private String owner;

    private LocalDateTime allocatedAt;

}