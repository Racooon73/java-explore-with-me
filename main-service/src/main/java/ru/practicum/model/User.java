package ru.practicum.model;

import lombok.*;
import ru.practicum.model.dto.UserShortDto;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(name = "User.findByIdShort",
        query = "select u.id as id, u.name as name from users u where id = :id",
        resultSetMapping = "Mapping.UserShortDto")
@SqlResultSetMapping(name = "Mapping.UserShortDto",
        classes = @ConstructorResult(targetClass = UserShortDto.class,
                columns = {@ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class)}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Email
    private String email;
}
