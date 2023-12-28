
import { AUTH_PATH } from 'constant';
import Header from 'layouts/Header'
import Footer from 'layouts/footer'
import React from 'react'
import { Outlet, useLocation } from 'react-router-dom'

//	   component: 컨테이너 컴포넌트        //
export default function Container() {

	//      state: 현재 페이지의 path name		//
	const { pathname } = useLocation();

	//	   render: 컨테이너 레이아웃 렌더링        //
  return (
	<>
	  <Header />
	  <Outlet />
	  {pathname !== AUTH_PATH() && <Footer />}
	</>
  )
}
