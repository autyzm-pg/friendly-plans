package database.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Asset {

    @Id(autoincrement = true)
    private Long id;

    private String type;

    private String filename;

    @Generated(hash = 1968930852)
    public Asset(Long id, String type, String filename) {
        this.id = id;
        this.type = type;
        this.filename = filename;
    }

    @Generated(hash = 259099217)
    public Asset() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
