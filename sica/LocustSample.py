from locust import HttpLocust, TaskSet, task

class WebsiteTasks(TaskSet):
 @task
 def about(self):
     self.client.get("$request_path")

class WebsiteUser(HttpLocust):
 task_set = WebsiteTasks
 min_wait = $min_wait
 max_wait = $max_wait
