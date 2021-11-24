export let data =  {

    "nodes" :[
        {"id": "Mango", "group": 1},
        {"id": "Apple", "group": 1},
        {"id": "Orange", "group": 1},
        {"id": "Pinapple", "group": 1},
        {"id": "Strawberry", "group": 1},
        {"id": "Watermelon", "group": 1},
    ],

    "links": [
        {"source": "Mango", "target": "Apple", "value": 1},
        {"source": "Apple", "target": "Orange", "value": 8},
        {"source": "Orange", "target": "Pinapple", "value": 10},
        {"source": "Pinapple", "target": "Watermelon", "value": 6},
        {"source": "Pinapple", "target": "Strawberry", "value": 6},
    ]
}