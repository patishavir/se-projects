package oz.temp.json.jackson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Library {
	@JsonProperty("libraryname")
	public String name;
	@JsonProperty("mymusic")
	public List<Song> songs;

	public String getAsString() {
		return "name: " + name + " song 1: " + songs.get(0);
	}
}
