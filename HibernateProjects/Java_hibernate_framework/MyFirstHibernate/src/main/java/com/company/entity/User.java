package com.company.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByFirstname", query = "SELECT u FROM User u WHERE u.firstname = :firstname"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByAge", query = "SELECT u FROM User u WHERE u.age = :age")})
public class User implements Serializable {
    private static final long serialVersionUID = 5317714804438241788L;
    @Id //คีย์หลัก
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    //optional = true สามารถ เพิ่ม,แก้ไข,ลบ ได้โดยไม่ต้องกำหนดให้หมดทุก properties
    //optional = false จะตรงกันข้าม
    @Basic(optional = true) 
    @Column(name = "firstname")
    private String firstname;
    @Basic(optional = true)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = true)
    @Column(name = "age")
    private int age;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String firstname, String surname, int age) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", firstname=" + firstname + ", surname=" + surname + ", age=" + age + '}';
    }

    
}
