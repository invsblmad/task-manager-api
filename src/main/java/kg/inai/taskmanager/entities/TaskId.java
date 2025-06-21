package kg.inai.taskmanager.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TaskId implements Serializable {

    private String projectCode;
    private Long sequenceNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskId that)) return false;
        return Objects.equals(projectCode, that.projectCode) &&
                Objects.equals(sequenceNumber, that.sequenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectCode, sequenceNumber);
    }
}
