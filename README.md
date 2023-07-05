# campweek1
프로젝트 이름: MyApplication 

팀원: 김하진, 김현서

개발언어: kotlin

프로젝트 설명: 

세 개의 탭으로 구성되어 있는 앱이다.

<img src="https://github.com/gkwls1012/campweek1/assets/138105180/36e1e2c2-1a17-4e55-9ad2-9ab54cb03d74.png" width="200" height="400"/>
<img src="https://github.com/gkwls1012/campweek1/assets/138105180/f4926beb-cf97-4ae6-9840-90077001e89c.png" width="200" height="400"/>

1. Contacts
   - 휴대폰의 연락처 데이터를 활용하여 연락처를 볼 수 있다.
   - READ_CONTACTS로 권한을 받아와 핸드폰 주소록에 저장된 연락처를 불러온 뒤, RecyclerView로 화면에 나타낸다.
   - Add Contact
        *  AddContactFragment로 이동한다.
        *  해당 화면에서 연락처를 직접 추가할 수 있다. EditText를 활용해 사용자에게 이름과 전화번호를 입력받는다.
   - Done
        *  이름과 전화번호를 입력했을 경우 연락처를 추가한다. 이름과 전화번호 중 하나라도 누락된 경우, "Please enter name and phone number" 문구가 화면에 출력된다.
        *  WRITE_CONTACTS로 권한을 받아 RecyclerView뿐만 아닌 핸드폰 연락처 앱에도 연락처가 추가된다.
   - 휴대폰 연락처 데이터와 연동시켜, 앱을 종료한 후 들어와도 연락처들이 남아있도록 했다.

<img src="https://github.com/gkwls1012/campweek1/assets/138105180/203dda34-2dbe-4605-a0fe-177b659c78fb.png" width="200" height="400"/> 
<img src="https://github.com/gkwls1012/campweek1/assets/138105180/38d69800-058e-4f6f-9fad-d3ab2092f3f0.png" width="200" height="400"/>

2. Gallery
   - 원하는 사진만을 모아놓은 자신만의 사진 앨범을 만들 수 있다.
   - imageList의 사진들을 RecyclerView와 Glide를 이용해 화면에 띄워준다.
   - Add Photo
        *  핸드폰 갤러리에 저장된 사진을 불러온다.
        *  READ_EXTERNAL_STORAGE로 권한을 받아와 local storage의 선택된 사진들의 uri를 imageList에 추가한다.
   - Delete Photo
        *   이미지를 선택해 imageList에서 삭제한다. (여러 장 선택 가능)
   - savedImageList를 이용해 앱을 종료한 후 들어와도 사진 앨범의 사진들이 남아있도록 했다.

<img src="https://github.com/gkwls1012/campweek1/assets/138105180/9913f2f7-80e7-461c-a7df-69d7c7bf4074.png" width="200" height="400"/>  
<img src="https://github.com/gkwls1012/campweek1/assets/138105180/eef83845-da7d-41f9-8f87-e61fb47415d0.png" width="200" height="400"/>

3. MiniGame
   - 공룡알을 탭하면 공룡 캐릭터가 나오는 미니 게임이다.
   - 탭 횟수는 10~50 사이 랜덤으로 설정되며, 횟수를 채우면 간단한 애니메이션과 함께 4종류의 공룡 캐릭터가 나온다.
   - Restart 버튼을 눌러 게임을 다시 진행할 수 있다.
   - 화면 하단에는 지금까지 각 공룡 캐릭터가 총 몇 마리 부화했는지 텍스트로 보여진다.
   - 간단한 코드들로만 기능이 구성되어 있다.
   
*APK file: github repository에 업로드됨. (app-debug.apk)
