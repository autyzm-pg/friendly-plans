package model;

import java.io.Serializable;
import java.util.List;

public class ChildActivity implements Serializable {
	
	private long id;
	private String title;
	private List<Slide> slides;
    private String typeFlag;
    public ChildActivity()
    {

    }
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public String getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(String flag) {
        this.typeFlag = flag;
    }

	public void setSlides(List<Slide> slides){
		this.slides=slides;
	}

	public List<Slide> getSlides(){
		return slides;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return title;
	}

}
