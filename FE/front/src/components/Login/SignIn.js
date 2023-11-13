import React, { Component } from "react";
import { Link } from "react-router-dom";
import "../../styles/components/Login/SignIn.css";
import HorizonLine from "../../utils/HorizontalLine";

class SignIn extends Component {
  state = {
    email: "",
    password: "",
  };

  loginHandler = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  loginClickHandler = () => {
    const { email, password } = this.state;
    fetch("http://localhost:8000/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email,
        password,
      }),
    })
      .then((res) => res.json())
      .then((res) => console.log(res));
  };

  render() {
    const { isOpen, close } = this.props;
    return (
      <>
        {isOpen ? (
          <div className="modal">
            <div onClick={close}>
              <div className="loginModal">
                <span className="close" onClick={close}>
                  &times;
                </span>
                <div className="modalContents" onClick={isOpen}>
                  <div className="socialBox">
                    <div className="google">
                      <div className="googleText">구글 계정으로 로그인</div>
                    </div>
                    <div className="kakao">
                      <div className="kakaoText">카카오 계정으로 로그인</div>
                    </div>
                  </div>
                  <HorizonLine text="또는" />
                  <input
                    name="email"
                    className="inputId"
                    type="text"
                    placeholder="아이디"
                    onChange={this.loginHandler}
                  />
                  <input
                    name="password"
                    className="inputPw"
                    type="password"
                    placeholder="비밀번호"
                    onChange={this.loginHandler}
                  />
                  <div className="loginMid">
                    <label className="autoLogin" for="hint">
                      {" "}
                      <input type="checkbox" id="hint" /> 로그인 유지하기
                    </label>
                    <div className="toSignin">회원가입</div>
                  </div>
                  <button className="loginBtn" onClick={this.loginClickHandler}>
                    {" "}
                    로그인{" "}
                  </button>

                  <div className="loginEnd"></div>
                </div>
              </div>
            </div>
          </div>
        ) : null}
      </>
    );
  }
}

export default SignIn;
