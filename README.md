# 멋사스네스

## ABOUT
멋쟁이 사자처럼 + SNS의 합성어로 사이트의 로그인해서 다른 전세계의 유저들과 소통이 가능한 SNS입니다

## 사용 스택, 환경
- Java, Spring
- MySql
- Docker
- AWS

## EndPoint

- 회원가입
```
POST /api/v1/users/join

{
    "userName":"유저이름",
    "password":"비밀번호" 
}
```
- 로그인
```
POST /api/v1/users/login

{
    "userName":"유저이름",
    "password":"비밀번호" 
}
```
- 글 작성
```
POST /api/v1/posts

{
    "title":"제목",
    "body":"내용" 
}
```
- 글 목록
```
GET /api/v1/posts
```
- 글 상세정보
```
GET /api/v1/posts/{post-id}
```
- 글 수정
```
PUT /api/v1/posts/{post-id}

{
    "title":"제목",
    "body":"내용" 
}
```