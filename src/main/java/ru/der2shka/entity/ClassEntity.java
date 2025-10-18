package ru.der2shka.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "study_classes")
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "seq_num")
    private Integer seqNum;

    @Column(name = "time_period", length = 20)
    private String timePeriod;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "teacher_fio")
    private String teacherFio;

    @Column(name = "address", length = 255)
    private String address;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compare this object and entity fields excluding id filed on equals.
     * @param entity {@link ClassEntity} object;
     * @return are equals this object and entity.
     * **/
    public boolean equalsByFieldsExcludeId(ClassEntity entity) {
        return this.date.equals(entity.date) &&
                this.seqNum == entity.seqNum &&
                this.timePeriod.equals(entity.timePeriod) &&
                this.name.equals(entity.name) &&
                this.teacherFio.equals(entity.teacherFio) &&
                this.address.equals(entity.address);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;

        if (o == this) return true;

        if (!(o instanceof ClassEntity oce)) return false;

        return this.hashCode() == oce.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("ID: %d%n", this.id));
        sb.append(String.format("Date: %s%n", this.date));
        sb.append(String.format("SequenceNum: %d%n: ", this.seqNum));
        sb.append(String.format("Time period: %s%n", this.timePeriod));
        sb.append(String.format("Name: %s%n", this.name));
        sb.append(String.format("Teacher: %s%n", this.teacherFio));
        sb.append(String.format("Address: %s%n", this.address));

        return sb.toString();
    }
}
