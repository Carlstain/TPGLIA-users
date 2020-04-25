package series.users.models;

public class Serie {
    private Long id;
    private String title;
    private String description;
    private Long userid;

    public Serie() {

    }

    public Serie(Long id, String title, String description, Long userid){
        this.id = id;
        this.title = title;
        this.description = description;
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
