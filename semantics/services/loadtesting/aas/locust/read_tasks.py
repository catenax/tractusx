import json
import urllib.parse

from collections import deque

from locust import HttpUser, task, tag, constant_throughput


def read_shell_data():
    with open("shell_data.json", "r", encoding="utf-8") as shell_data_file:
        return deque([json.loads(entry) for entry in shell_data_file.readlines()])

SHELL_DATA = read_shell_data()

class ShellDescriptorRetrievalTask(HttpUser):
    
    # 100 Users * 0.1 = 10 req/s
    wait_time = constant_throughput(0.1)

    def on_start(self):
        self.shell = self.get_shell_data_entry()
    
    @tag('getShellDescriptorById')
    @task
    def getById(self):
        shell_id = self.shell['id']
        self.client.get(f"/registry/shell-descriptors/{shell_id}", name = "/registry/shell-descriptors/{id}" )

    @tag('lookupBySpecificAssetIds')
    @task
    def lookup(self):
        specificAssetIds = self.shell['specificAssetIds']
        decodedAssetIds = urllib.parse.quote_plus(json.dumps(specificAssetIds))
        self.client.get(f"/lookup/shells?assetIds={decodedAssetIds}", name = "/lookup/shells?assetIds={assetIds}")

    def get_shell_data_entry(self):
        if len(SHELL_DATA) > 0:
            return SHELL_DATA.pop()
        self.environment.runner.quit()
        return None
