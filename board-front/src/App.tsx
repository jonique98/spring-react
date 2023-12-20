import React from 'react';
import { latestBoardListMock } from 'mocks';

import BoardItem from 'components/BoardListItem';

import './App.css';

function App() {
  return (
    <>
      {latestBoardListMock.map(boardListItem => <BoardItem boardListItem={boardListItem}/>)}
    </>
  );
}

export default App;
