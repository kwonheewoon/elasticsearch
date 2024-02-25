package org.khw.elasticsearch.service

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.GetRequest
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.bulk.*
import lombok.RequiredArgsConstructor
import org.khw.elasticsearch.index.MyIndexClass
import org.khw.elasticsearch.index.MyPartialIndexClass
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
@RequiredArgsConstructor
class IndexerService(
    val client: ElasticsearchClient
) {

    fun indexExample() {
        val indexResponse = client.index {
            builder: IndexRequest.Builder<MyIndexClass> ->
            builder
                .index("my-index")
                .id("my-id-1")
                .routing("my-routng-1")
                .document(MyIndexClass("hello", 1L, ZonedDateTime.now()))

        }

        val result = indexResponse.result()

        println(result.name)
    }

    fun getIndexExample() {
        val getRequest = GetRequest.Builder()
            .index("my-index")
            .id("my-id-1")
            .routing("my-routing-1")
            .build()

        val response = client.get(getRequest, MyIndexClass::class.java)
        println("response = " + response.source())
    }

    // 빌더 패턴만으로 bulk API 호출
//    fun builkExampleOne() {
//
//        // 문서가 이미 존재하면 문서 생성 실패
//        // 문서 아이디는 ES가 유니크한 값으로 임의 생성
//        val createOperation = CreateOperation.Builder<MyIndexClass>()
//            .index("my-index")
//           // .id("my-id-4")
//            .routing("my-routing-4")
//            .document(MyIndexClass("world", 2L, ZonedDateTime.now()))
//            .build()
//
//        // 문서가 이미 존재하면 문서 업데이트
//        // 문서 아이디는 ES가 유니크한 값으로 임의 생성
//        val indexOperation = IndexOperation.Builder<MyIndexClass>()
//            .index("my-index")
//            //.id("my-id-5")
//            .routing("my-routing-5")
//            .document(MyIndexClass("world", 4L, ZonedDateTime.now()))
//            .build()
//
//        val updateAction = UpdateAction.Builder<MyIndexClass, MyPartialIndexClass>()
//            .doc(MyPartialIndexClass("world updated"))
//            .build()
//
//        val updateOperation = UpdateOperation.Builder<MyIndexClass, MyPartialIndexClass>()
//            .index("my-index")
//            .id("my-id-1")
//            .routing("my-rouint-1")
//            .action(updateAction)
//            .build()
//
//        val bulkOpOne = BulkOperation.Builder().create(createOperation).build()
//        val bulkOpTwo = BulkOperation.Builder().index(indexOperation).build()
//        val bulkOpThree = BulkOperation.Builder().update(updateOperation).build()
//
//        val operations = listOf<BulkOperation>(bulkOpOne, bulkOpTwo, bulkOpThree)
//        val bulkResponse = client.bulk { it.operations(operations) }
//
//        for(item in bulkResponse.items()){
//            println("result : ${item.result()}, error : ${item.error()}")
//        }
//    }

    // 함수형 호출 패턴으로 bulk API 호출
    fun builkExampleOne() {

        val bulkResponse = client.bulk { _0 -> _0
            .operations { _1 -> _1
                .index {_2: IndexOperation.Builder<MyIndexClass> -> _2
                    .index("my-index")
                    .id("my-id-6")
                    .routing("my-routing-6")
                    .document(MyIndexClass("world", 6L, ZonedDateTime.now()))
                }
            }
            .operations { _1 -> _1
                .update { _2: UpdateOperation.Builder<MyIndexClass, MyPartialIndexClass> -> _2
                    .index("my-index")
                    .id("my-id-2")
                    .routing("my-routing-2")
                    .action { _3 -> _3
                        .doc(MyPartialIndexClass("world updated22222"))
                    }
                }

            }
        }

        for(item in bulkResponse.items()){
            println("result : ${item.result()}, error : ${item.error()}")
        }
    }

    fun searchExample() {
        val response = client.search({builder -> builder
            .index("my-index")
            .from(0)
            .size(10)
            .query { query -> query
                .term { term -> term
                    .field("fieldOne")
                    .value { value -> value
                        .stringValue("world")
                    }
                }
            }}, MyIndexClass::class.java)

        for (hit in response.hits().hits()){
            println(hit.source())
        }
    }
}