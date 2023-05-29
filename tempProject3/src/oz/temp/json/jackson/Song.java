package oz.temp.json.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Song {
	@JsonProperty("Artist Name")
	public String artistName;
	@JsonProperty("Song Name")
	public String songName;
}
