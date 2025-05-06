from locust import HttpUser, task, between, TaskSet
from locust import events
from locust.runners import MasterRunner, WorkerRunner

### 테스팅 활용 
class UserBehavior(TaskSet):
    def on_start(self):
        res = self.client.post(
            "/api/v1/auth/login",
            json={
                "email": "testuser1234@test.com",
                "password": "qlalfqjsgh1234@"
            },
            verify=False
        )
        
        self.token = res.json().get('data').get('accessToken')

        if not self.token:
            print("[에러] 로그인 실패: token 없음")

    ### Spring MVC 백엔드 게시글 조회 테스트 
    @task
    def getArticles(self):
        if self.token:
            self.client.get(
                "/api/v1/articles",
                headers={
                    "Authorization": f"Bearer {self.token}"
                },
                verify=False
            )



    ### Spring Webflux 백엔드 조회 테스트 


class LocustUser(HttpUser):
    host = "https://localhost:8443"
    tasks = [ UserBehavior ]
    min_wait = 5000
    max_wait = 15000
