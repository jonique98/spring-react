import React from 'react'
import './style.css'

// 			component:  푸터 레이아웃 컴포넌트 			//
export default function Footer() {

	//					event handler: 인스타 아이콘 버튼 클릭 이벤트 핸들러			//
	const onInstaIconButtonClickHandler = () => {
		window.open('https://www.instagram.com/fxkcopy')
	}

	// 					event handler: 네이버 블로그 아이콘 버튼 클릭 이벤트 핸들러			//
	const onNaverBlogIconButtonClickHandler = () => {
		window.open('https://blog.naver.com/tnals9899')
	}

	// 			render: 푸터 레이아웃 컴포넌트 			//
  return (
	<div id='footer'>
	  <div className='footer-container'>
		<div className='footer-top'>
			<div className='footer-logo-box'>
				<div className='icon-box'>
					<div className='icon logo-light-icon'></div>
				</div>
				<div className='footer-logo-text'>{`Sumjo Board`}</div>
			</div>
			<div className='footer-link-box'>
				<div className='email-link'>{'tnals9899@naver.com'}</div>
				<div className='icon-button'>
					<div className='icon insta-icon' onClick={onInstaIconButtonClickHandler}></div>
				</div>
				<div className='icon-button'>
					<div className='icon naver-blog-icon' onClick={onNaverBlogIconButtonClickHandler}></div>
				</div>
			</div>
		</div>
		<div className='footer-bottom'>
			<div className='footer-copy-right'>{'Copyright ⓒ 2023 Sumjo. All Rights Reserved'}</div>
		</div>
	  </div>
	</div>
  )
}
