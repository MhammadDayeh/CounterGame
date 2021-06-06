package countgame;

import java.io.Serializable;

public class Player implements Serializable {
	private String name;
	private String score;

	Player() { }

	Player(String name, String score) {
		setName(name);
		setScore(score);
	}

	Player(String name) {
		setName(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(String score) {
		if (!(score.isEmpty())) this.score = score;
	}

	public String getName() {
		return this.name;
	}

	public String getScore() {
		return this.score;
	}

	public String toString() {
		return "Name: " + this.name + " Score: " + this.score;
	}
}
