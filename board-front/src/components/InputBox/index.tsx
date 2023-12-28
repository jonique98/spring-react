import { KeyboardEvent, ChangeEvent, Dispatch, SetStateAction, forwardRef } from 'react'
import './style.css'


//         interface: Input Box 컴포넌트 			// 
interface Props {
	label: string;
	type: 'text' | 'password';
	value: string;
	setValue: Dispatch<SetStateAction<string>>;
	placeholder: string;
	error: boolean;

	icon?: string;
	onButtonClick?: () => void;

	message?: string;

	onKeyDown?: (event: KeyboardEvent<HTMLInputElement>) => void;
}

//          component Input Box 컴포넌트 			//

const InputBox = forwardRef<HTMLInputElement, Props>((props: Props, ref) => {

	//         properties 			//
	const { label, type, value,  placeholder, error, icon, message} = props;
	const { setValue , onButtonClick, onKeyDown} = props;

	// .       event handlers 			//
	const onChangeHandler = (event : ChangeEvent<HTMLInputElement>) => {
		const { value } = event.target;
		setValue(value);
	}

	//       키 이벤트 처리 함수 			//
	const onKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) => {
		if(!onKeyDown) return;
		onKeyDownHandler(event);
	}

	//       render: Input Box 컴포넌트 			//
	return (
		<div className='inputbox'>
			<div className='inputbox-label'>{'비밀번호*'}</div>
			<div className={error ? 'inputbox-container-error' : 'inputbox-container'}>
				<input  ref={ref} type = {type} className='input' placeholder={placeholder} value={value} onChange={onChangeHandler} onKeyDown={onKeyDownHandler}/>
				{onButtonClick !== undefined && (
				<div className='icon-button'>
					{icon !== undefined && (
					<div className={`icon ${icon}`}></div>
					)}
				</div>
				)}
			</div>
			{message !== undefined && <div className='inputbox-message'>{message}</div>}
		</div>
	)
})

export default InputBox
