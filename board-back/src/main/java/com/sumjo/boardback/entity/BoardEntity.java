package com.sumjo.boardback.entity;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sumjo.boardback.dto.request.board.PostboardRequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="board")
@Table(name="board")
public class BoardEntity {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int boardNumber;

	private String title;
	private String content;
	private String writeDatetime;
	private int favoriteCount;
	private int commentCount;
	private int viewCount;
	private String writerEmail;


	public BoardEntity(PostboardRequestDto dto, String email) {

		Date now = Date.from(Instant.now());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String writeDatetime = simpleDateFormat.format(now);

		this.title = dto.getTitle();
		this.content = dto.getContent();
		this.writeDatetime = writeDatetime;
		this.favoriteCount = 0;
		this.commentCount = 0;
		this.viewCount = 0;
		this.writerEmail = email;
	}

	public void increaseViewCount() {
		this.viewCount++;
	}

	public void increaseFavoriteCount() {
		this.favoriteCount++;
	}

	public void decreaseFavoriteCount() {
		this.favoriteCount--;
	}
	
}
