package model;

import java.io.Serializable;

public class Slide implements Serializable{
	private long id;
	private String text;
    private int status;
    private String imagePath;
    private String audioPath;
    private int time;

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String title) {
		this.text = title;
	}


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public int getTime() {
        return time;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
