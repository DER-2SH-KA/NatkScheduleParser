package ru.der2shka.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
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
}
