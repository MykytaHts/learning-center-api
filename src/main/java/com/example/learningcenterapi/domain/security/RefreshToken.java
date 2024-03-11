package com.example.learningcenterapi.domain.security;

import com.example.learningcenterapi.domain.BaseEntity;
import com.example.learningcenterapi.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true, of = {"token"})
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_tokens", schema = "management")
@Entity
public class RefreshToken extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 7047899943693634046L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;
}
