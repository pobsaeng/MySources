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

@Entity //entity class
@Table(name = "tb_user") //ชื่อตาราง
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TbUser.findAll", query = "SELECT t FROM TbUser t"),
    @NamedQuery(name = "TbUser.findById", query = "SELECT t FROM TbUser t WHERE t.id = :id"),
    @NamedQuery(name = "TbUser.findByUser", query = "SELECT t FROM TbUser t WHERE t.user = :user"),
    @NamedQuery(name = "TbUser.findByPass", query = "SELECT t FROM TbUser t WHERE t.pass = :pass")})
public class TbUser implements Serializable {
    //เป็น id ระบุเพื่อให้รู้กันระหว่าง client และ server จะอ่านด้วยหมายเลข
    private static final long serialVersionUID = -7289780247152889496L;
    
    @Id //เปรียบกับ primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    @Basic(optional = false)//ใส่ไม่ใส่ก็ได้
    //Map กัน
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "user")
    private String user;
    @Basic(optional = true)
    @Column(name = "pass")
    private String pass;

    public TbUser() {
    }
//
    public TbUser(Integer id) {
        this.id = id;
    }

    public TbUser(Integer id, String user, String pass) {
        this.id = id;
        this.user = user;
        this.pass = pass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    //เปรียบเทียบ object
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TbUser)) {
            return false;
        }
        TbUser other = (TbUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    //ดีในเรื่องของ log
//    @Override
//    public String toString() {
//        return "com.mycompany.TbUser[ id=" + id + " ]";
//    }
    @Override
    public String toString() {
        return id + ", user=" + user + ", pass=" + pass + '}';
    }
    
    
}
