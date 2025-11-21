package com.example.todolist.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NoteDao_Impl implements NoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NoteEntity> __insertionAdapterOfNoteEntity;

  private final EntityInsertionAdapter<ChecklistItemEntity> __insertionAdapterOfChecklistItemEntity;

  private final EntityDeletionOrUpdateAdapter<NoteEntity> __deletionAdapterOfNoteEntity;

  private final EntityDeletionOrUpdateAdapter<ChecklistItemEntity> __deletionAdapterOfChecklistItemEntity;

  private final EntityDeletionOrUpdateAdapter<NoteEntity> __updateAdapterOfNoteEntity;

  private final EntityDeletionOrUpdateAdapter<ChecklistItemEntity> __updateAdapterOfChecklistItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteChecklistForNote;

  public NoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNoteEntity = new EntityInsertionAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `notes` (`note_id`,`title`,`body`,`created_at`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final NoteEntity entity) {
        statement.bindLong(1, entity.getNoteId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getBody() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBody());
        }
        statement.bindLong(4, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfChecklistItemEntity = new EntityInsertionAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `checklist_items` (`id`,`note_id`,`content`,`completed`,`position`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChecklistItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getNoteId());
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getPosition());
      }
    };
    this.__deletionAdapterOfNoteEntity = new EntityDeletionOrUpdateAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `notes` WHERE `note_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final NoteEntity entity) {
        statement.bindLong(1, entity.getNoteId());
      }
    };
    this.__deletionAdapterOfChecklistItemEntity = new EntityDeletionOrUpdateAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `checklist_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChecklistItemEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfNoteEntity = new EntityDeletionOrUpdateAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notes` SET `note_id` = ?,`title` = ?,`body` = ?,`created_at` = ? WHERE `note_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final NoteEntity entity) {
        statement.bindLong(1, entity.getNoteId());
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getBody() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBody());
        }
        statement.bindLong(4, entity.getCreatedAt());
        statement.bindLong(5, entity.getNoteId());
      }
    };
    this.__updateAdapterOfChecklistItemEntity = new EntityDeletionOrUpdateAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `checklist_items` SET `id` = ?,`note_id` = ?,`content` = ?,`completed` = ?,`position` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChecklistItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getNoteId());
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getPosition());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteChecklistForNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM checklist_items WHERE note_id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insertNote(final NoteEntity note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfNoteEntity.insertAndReturnId(note);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertChecklistItems(final List<ChecklistItemEntity> items) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChecklistItemEntity.insert(items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteNote(final NoteEntity note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfNoteEntity.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteChecklistItem(final ChecklistItemEntity item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfChecklistItemEntity.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateNote(final NoteEntity note) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfNoteEntity.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateChecklistItem(final ChecklistItemEntity item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfChecklistItemEntity.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteChecklistForNote(final long noteId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteChecklistForNote.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, noteId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteChecklistForNote.release(_stmt);
    }
  }

  @Override
  public LiveData<List<NoteWithChecklist>> getNotes() {
    final String _sql = "SELECT * FROM notes ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"checklist_items",
        "notes"}, true, new Callable<List<NoteWithChecklist>>() {
      @Override
      @Nullable
      public List<NoteWithChecklist> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfNoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "note_id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfNoteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfNoteId);
              }
              if (_tmpKey != null) {
                if (!_collectionItems.containsKey(_tmpKey)) {
                  _collectionItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
                }
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomExampleTodolistDataChecklistItemEntity(_collectionItems);
            final List<NoteWithChecklist> _result = new ArrayList<NoteWithChecklist>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final NoteWithChecklist _item;
              final NoteEntity _tmpNote;
              if (!(_cursor.isNull(_cursorIndexOfNoteId) && _cursor.isNull(_cursorIndexOfTitle) && _cursor.isNull(_cursorIndexOfBody) && _cursor.isNull(_cursorIndexOfCreatedAt))) {
                final String _tmpTitle;
                if (_cursor.isNull(_cursorIndexOfTitle)) {
                  _tmpTitle = null;
                } else {
                  _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                }
                final String _tmpBody;
                if (_cursor.isNull(_cursorIndexOfBody)) {
                  _tmpBody = null;
                } else {
                  _tmpBody = _cursor.getString(_cursorIndexOfBody);
                }
                final long _tmpCreatedAt;
                _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
                _tmpNote = new NoteEntity(_tmpTitle,_tmpBody,_tmpCreatedAt);
                final long _tmpNoteId;
                _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
                _tmpNote.setNoteId(_tmpNoteId);
              } else {
                _tmpNote = null;
              }
              final ArrayList<ChecklistItemEntity> _tmpItemsCollection;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfNoteId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfNoteId);
              }
              if (_tmpKey_1 != null) {
                _tmpItemsCollection = _collectionItems.get(_tmpKey_1);
              } else {
                _tmpItemsCollection = new ArrayList<ChecklistItemEntity>();
              }
              _item = new NoteWithChecklist();
              _item.note = _tmpNote;
              _item.items = _tmpItemsCollection;
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<NoteWithChecklist> getNote(final long id) {
    final String _sql = "SELECT * FROM notes WHERE note_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"checklist_items",
        "notes"}, true, new Callable<NoteWithChecklist>() {
      @Override
      @Nullable
      public NoteWithChecklist call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfNoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "note_id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfNoteId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfNoteId);
              }
              if (_tmpKey != null) {
                if (!_collectionItems.containsKey(_tmpKey)) {
                  _collectionItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
                }
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomExampleTodolistDataChecklistItemEntity(_collectionItems);
            final NoteWithChecklist _result;
            if (_cursor.moveToFirst()) {
              final NoteEntity _tmpNote;
              if (!(_cursor.isNull(_cursorIndexOfNoteId) && _cursor.isNull(_cursorIndexOfTitle) && _cursor.isNull(_cursorIndexOfBody) && _cursor.isNull(_cursorIndexOfCreatedAt))) {
                final String _tmpTitle;
                if (_cursor.isNull(_cursorIndexOfTitle)) {
                  _tmpTitle = null;
                } else {
                  _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
                }
                final String _tmpBody;
                if (_cursor.isNull(_cursorIndexOfBody)) {
                  _tmpBody = null;
                } else {
                  _tmpBody = _cursor.getString(_cursorIndexOfBody);
                }
                final long _tmpCreatedAt;
                _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
                _tmpNote = new NoteEntity(_tmpTitle,_tmpBody,_tmpCreatedAt);
                final long _tmpNoteId;
                _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
                _tmpNote.setNoteId(_tmpNoteId);
              } else {
                _tmpNote = null;
              }
              final ArrayList<ChecklistItemEntity> _tmpItemsCollection;
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfNoteId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfNoteId);
              }
              if (_tmpKey_1 != null) {
                _tmpItemsCollection = _collectionItems.get(_tmpKey_1);
              } else {
                _tmpItemsCollection = new ArrayList<ChecklistItemEntity>();
              }
              _result = new NoteWithChecklist();
              _result.note = _tmpNote;
              _result.items = _tmpItemsCollection;
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipchecklistItemsAscomExampleTodolistDataChecklistItemEntity(
      @NonNull final LongSparseArray<ArrayList<ChecklistItemEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipchecklistItemsAscomExampleTodolistDataChecklistItemEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`note_id`,`content`,`completed`,`position` FROM `checklist_items` WHERE `note_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "note_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfNoteId = 1;
      final int _cursorIndexOfContent = 2;
      final int _cursorIndexOfCompleted = 3;
      final int _cursorIndexOfPosition = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ChecklistItemEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ChecklistItemEntity _item_1;
          final long _tmpNoteId;
          _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
          final String _tmpContent;
          if (_cursor.isNull(_cursorIndexOfContent)) {
            _tmpContent = null;
          } else {
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
          }
          final boolean _tmpCompleted;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfCompleted);
          _tmpCompleted = _tmp != 0;
          final int _tmpPosition;
          _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
          _item_1 = new ChecklistItemEntity(_tmpNoteId,_tmpContent,_tmpCompleted,_tmpPosition);
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          _item_1.setId(_tmpId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
