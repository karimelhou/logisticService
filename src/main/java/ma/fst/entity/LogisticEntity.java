package ma.fst.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "logistic")
public class LogisticEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logisticId;

    private TypeLogistic type;

    private Status status;

    private Integer userId;

    public LogisticEntity() {
    }

    public LogisticEntity(Long logisticId, TypeLogistic type, Status status, Integer userId) {
        this.logisticId = logisticId;
        this.type = type;
        this.status = status;
        this.userId = userId;
    }

    public Long getLogisticId() {
        return logisticId;
    }

    public void setLogisticId(Long logisticId) {
        this.logisticId = logisticId;
    }

    public TypeLogistic getType() {
        return type;
    }

    public void setType(TypeLogistic type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
