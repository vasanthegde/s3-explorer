package com.cloud

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request

data class Tree(val name: String, var children: MutableMap<String, Tree> = mutableMapOf(), val isFile: Boolean = false)


class StorageClient {
    private val root = Tree("root")

    init {
        generateFolder()
    }

    private fun generateFolder() {
        val client = S3Client.builder().region(Region.US_EAST_1).build()
        val request = ListObjectsV2Request.builder().bucket("avacado").prefix("").build()
        val response = client.listObjectsV2Paginator(request)
        for (page in response) {
            page.contents().forEach {
                val parts = it.key().split("/")
                recursive(0, root, parts, it.size())
            }
        }

        println(root.children.keys)
        println("done generating trie")
    }


    private fun recursive(idx: Int, tree: Tree?, parts: List<String>, fileSize: Long) {
        if (idx >= parts.size) return
        val current = parts[idx]
        if (!tree?.children?.containsKey(current)!!) {
            val isFile = idx == parts.size - 1 && fileSize > 0
            tree?.children?.set(current, Tree(current, isFile = isFile))
        }
        recursive(idx + 1, tree?.children?.get(current), parts, fileSize)
    }

    fun getFolders(name: String): MutableSet<String> {
        return getFolders(root, name)
    }

    fun getFolders(root: Tree, name: String): MutableSet<String> {
        if (name.isNullOrEmpty()) return mutableSetOf()

        if (root.children.containsKey(name)) {
            return root.children[name]?.children?.keys ?: mutableSetOf()
        }

        val parts = name.split(":")
        if (parts.size <= 1) return mutableSetOf()

        val prefix = parts.first()
        if (root.children.containsKey(prefix)) {
            return getFolders(root.children[prefix]!!, name.removePrefix("$prefix:"))
        }

        return mutableSetOf()

    }

    fun getBaseFolders(): MutableSet<String> {
        return root.children.keys
    }
}