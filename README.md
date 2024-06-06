
# Movie Recommender
> **부산대학교 정보컴퓨터공학부 <소프트웨어설계및실험> 과목 Term project** <br/> **개발기간: 2024.05 ~ 2024.06**

## 프로젝트 소개

Movie Recommender는 부산대학교 내 과목 중 하나인 <소프트웨어설계및실험> 과목의 term project의 결과물이며, 대략 2주 정도 개발하였습니다.
주제는 자유주제로 수업 시간에 배운 안드로이드를 활용하여 어플리케이션을 만드는 것이었고, 저희는 AI에 관심이 있어 안드로이드 내에서 추천시스템을 구현해보았습니다.

- 영화 목록 및 상세 정보는 [TMDB API](https://developer.themoviedb.org/reference/intro/getting-started)를 사용하여 가져오고, 추천 시스템엔 [TMDB 5000 Movie Dataset](https://www.kaggle.com/datasets/tmdb/tmdb-movie-metadata) 파일을 이용합니다.

- Content-Based 방식의 추천 시스템을 사용하며, 유저가 영화를 선택했을 때 해당 영화의 정보만을 이용해 유사한 영화를 추천합니다.

- 파이프라인은 Kotlin으로 구현했으며 문장 임베딩에 사용한 DistilBERT 모델은 .ptl 파일로 만들어 프로젝트의 asset 디렉토리 내에 위치하고 사용합니다.


## 팀 소개

|      김명석       |          정지윤         |                                                                                                               
| :------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------: |
|   <img width="160px" src="https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/b335b91f-c0eb-4646-a401-622e35837896" />    |                      <img width="160px" src="https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/fe597c6b-2657-4319-bc14-ba4f49960452" />    |
|   UI/Design 개발   |  ML/DL 개발  |
|   [@mangsgi](https://github.com/mangsgi)   |    [@enchantee00](https://github.com/enchantee00)  |
| 부산대학교 정보컴퓨터공학부 3학년 | 부산대학교 정보컴퓨터공학부 3학년 |


## Stacks 🐈

### OS
![Android](https://img.shields.io/badge/Android-34A853?style=for-the-badge&logo=Android&logoColor=white)

### Environment
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=Android%20Studio&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)             

### Development
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=61DAFB)


---
## 화면 구성 📺
![Movie Recommender-2](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/9421a34e-9447-4525-954d-91ff6a789151)

---
## 주요 기능 📦

### ⭐️ 추천 시스템 개발
![6](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/e8e51eef-36fa-4742-a63b-ae5970146b85)
![7](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/fe14714e-5ed5-40a3-a066-11941222b47a)
![8](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/d9bec6df-1aeb-41cf-acfa-5c6f490f19cc)

### ⭐️ UI 개발
![9](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/80545926-21b1-40ad-9fdf-ce4de46a325b)
![10](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/3929914c-dd99-4fa8-b435-354eeb1da42d)
![11](https://github.com/Android-Term-Project/Movie-Recommender/assets/62553866/985f0c60-d611-4c4b-aba4-71ce5cbb215e)



