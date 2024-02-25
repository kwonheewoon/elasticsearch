package org.khw.elasticsearch.controller

import lombok.RequiredArgsConstructor
import org.khw.elasticsearch.service.IndexerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class IndexerController(
    val indexerService: IndexerService
) {

    @PostMapping("/create-document")
    fun createDocument(){
        indexerService.indexExample()
    }

    @GetMapping("/get-document")
    fun getDocument(){
        indexerService.getIndexExample()
    }

    @PostMapping("/bulk-document")
    fun bulkDocument(){
        indexerService.builkExampleOne()
    }

    @GetMapping("/search-document")
    fun searchDocument() {
        indexerService.searchExample()
    }

}