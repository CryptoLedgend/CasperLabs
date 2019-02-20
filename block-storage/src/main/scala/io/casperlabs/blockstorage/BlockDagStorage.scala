package io.casperlabs.blockstorage

import com.google.protobuf.ByteString
import io.casperlabs.blockstorage.BlockDagRepresentation.Validator
import io.casperlabs.blockstorage.BlockStore.BlockHash
import io.casperlabs.casper.protocol.BlockMessage
import io.casperlabs.models.BlockMetadata

trait BlockDagStorage[F[_]] {
  def getRepresentation: F[BlockDagRepresentation[F]]
  def insert(block: BlockMessage): F[Unit]
  def checkpoint(): F[Unit]
  def clear(): F[Unit]
  def close(): F[Unit]
}

object BlockDagStorage {
  def apply[F[_]](implicit B: BlockDagStorage[F]): BlockDagStorage[F] = B
}

trait BlockDagRepresentation[F[_]] {
  def children(blockHash: BlockHash): F[Option[Set[BlockHash]]]
  def lookup(blockHash: BlockHash): F[Option[BlockMetadata]]
  def contains(blockHash: BlockHash): F[Boolean]
  def topoSort(startBlockNumber: Long): F[Vector[Vector[BlockHash]]]
  def topoSortTail(tailLength: Int): F[Vector[Vector[BlockHash]]]
  def deriveOrdering(startBlockNumber: Long): F[Ordering[BlockMetadata]]
  def latestMessageHash(validator: Validator): F[Option[BlockHash]]
  def latestMessage(validator: Validator): F[Option[BlockMetadata]]
  def latestMessageHashes: F[Map[Validator, BlockHash]]
  def latestMessages: F[Map[Validator, BlockMetadata]]
}

object BlockDagRepresentation {
  type Validator = ByteString
}
