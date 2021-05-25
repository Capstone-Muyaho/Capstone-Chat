package com.example.capstone_frontend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.system.Os.socket
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.example.capstone_frontend.databinding.ActivityChatBinding
import com.example.capstone_frontend.MessageData
import com.example.capstone_frontend.RoomData
import com.example.capstone_frontend.Result
import com.example.capstone_frontend.RetrofitClient
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.Socket
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private var binding: ActivityChatBinding? = null
    private var mSocket: Socket? = null
    private var retrofitClient: RetrofitClient? = null
    private var username: String? = null
    private var roomNumber: String? = null
    private var adapter: ChatAdapter? = null
    private val gson = Gson()
    private val SELECT_IMAGE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        init()
    }

    private fun init() {
        retrofitClient = RetrofitClient.instance

        // MainActivity로부터 username과 roomNumber를 넘겨받음
        val intent = intent
        username = intent.getStringExtra("username")
        roomNumber = intent.getStringExtra("roomNumber")
        adapter = ChatAdapter(applicationContext)
        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.setLayoutManager(layoutManager)
        binding.recyclerView.setAdapter(adapter)


        // 메시지 전송 버튼
        binding.sendBtn.setOnClickListener({ v -> sendMessage() })
        // 이미지 전송 버튼
        binding.imageBtn.setOnClickListener({ v ->
            val imageIntent = Intent(Intent.ACTION_PICK)
            imageIntent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            startActivityForResult(imageIntent, SELECT_IMAGE)
        })
        try {
            mSocket = IO.socket("http://10.0.2.2:80")
            Log.d("SOCKET", "Connection success : " + mSocket.id())
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        mSocket.connect()
        mSocket.on(
            Socket.EVENT_CONNECT,
            { args -> mSocket.emit("enter", gson.toJson(RoomData(username, roomNumber))) })
        mSocket.on("update", { args ->
            val data: MessageData = gson.fromJson(args.get(0).toString(), MessageData::class.java)
            addChat(data)
        })
    }

    // 리사이클러뷰에 채팅 추가
    private fun addChat(data: MessageData) {
        runOnUiThread {
            if (data.type == "ENTER" || data.type == "LEFT") {
                adapter!!.addItem(
                    ChatItem(
                        data.from,
                        data.content,
                        toDate(data.sendTime),
                        ChatType.CENTER_MESSAGE
                    )
                )
                binding?.recyclerView?.scrollToPosition(adapter!!.itemCount - 1)
            } else if (data.type == "IMAGE") {
                if (data.from == username) {
                    adapter!!.addItem(
                        ChatItem(
                            data.from,
                            data.content,
                            toDate(data.sendTime),
                            ChatType.RIGHT_IMAGE
                        )
                    )
                } else {
                    adapter!!.addItem(
                        ChatItem(
                            data.from,
                            data.content,
                            toDate(data.sendTime),
                            ChatType.LEFT_IMAGE
                        )
                    )
                }
                binding?.recyclerView?.scrollToPosition(adapter!!.itemCount - 1)
            } else {
                if (data.from == username) {
                    adapter!!.addItem(
                        ChatItem(
                            data.from,
                            data.content,
                            toDate(data.sendTime),
                            ChatType.RIGHT_MESSAGE
                        )
                    )
                } else {
                    adapter!!.addItem(
                        ChatItem(
                            data.from,
                            data.content,
                            toDate(data.sendTime),
                            ChatType.LEFT_MESSAGE
                        )
                    )
                }
                binding?.recyclerView?.scrollToPosition(adapter!!.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        mSocket.emit(
            "newMessage", gson.toJson(
                MessageData(
                    "MESSAGE",
                    username,
                    roomNumber,
                    binding.contentEdit.getText().toString(),
                    System.currentTimeMillis()
                )
            )
        )
        Log.d(
            "MESSAGE", MessageData(
                "MESSAGE",
                username,
                roomNumber,
                binding.contentEdit.getText().toString(),
                System.currentTimeMillis()
            ).toString()
        )
        adapter!!.addItem(
            ChatItem(
                username,
                binding?.contentEdit?.getText().toString(),
                toDate(System.currentTimeMillis()),
                ChatType.RIGHT_MESSAGE
            )
        )
        binding?.recyclerView?.scrollToPosition(adapter!!.itemCount - 1)
        binding?.contentEdit?.setText("")
    }

    // 이미지 uri로부터 실제 파일 경로를 알아냄
    private fun getRealPathFromURI(
        contentUri: Uri?,
        context: Context
    ): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(context, contentUri!!, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    // Node.js 서버에 이미지를 업로드
    fun uploadImage(imageUri: Uri?, context: Context) {
        val image = File(getRealPathFromURI(imageUri, context))
        val requestBody =
            RequestBody.create(MediaType.parse("image/*"), image)
        val body =
            MultipartBody.Part.createFormData("image", image.name, requestBody)
        RetrofitClient.apiService.uploadImage(body)
            ?.enqueue(object : Callback<Result?> {
                override fun onResponse(
                    call: Call<Result?>,
                    response: Response<Result?>
                ) {
                    val result = response.body()
                    if (result!!.result == 1) {
                        Log.d("PHOTO", "Upload success : " + result.imageUri)
                        result.imageUri?.let { sendImage(it) }
                    } else {
                        Log.d("PHOTO", "Upload failed")
                    }
                }

                override fun onFailure(
                    call: Call<Result?>,
                    t: Throwable
                ) {
                    Log.d("PHOTO", "Upload failed : " + t.message)
                }
            })
    }

    private fun sendImage(imageUri: String) {
        mSocket.emit(
            "newImage", gson.toJson(
                MessageData(
                    "IMAGE",
                    username,
                    roomNumber,
                    imageUri,
                    System.currentTimeMillis()
                )
            )
        )
        Log.d(
            "IMAGE", MessageData(
                "IMAGE",
                username,
                roomNumber,
                imageUri,
                System.currentTimeMillis()
            ).toString()
        )
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private fun toDate(currentMiliis: Long): String {
        return SimpleDateFormat("hh:mm a").format(Date(currentMiliis))
    }

    // 이미지를 갤러리에서 선택했을 때 이벤트
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val selectedImageUri: Uri?
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            uploadImage(selectedImageUri, applicationContext)
            adapter!!.addItem(
                ChatItem(
                    username,
                    selectedImageUri.toString(),
                    toDate(System.currentTimeMillis()),
                    ChatType.RIGHT_IMAGE
                )
            )
            binding?.recyclerView?.scrollToPosition(adapter!!.itemCount - 1)
        }
    }

    // 이전 버튼을 누를 시 방을 나가고 소켓 통신 종료
    override fun onDestroy() {
        super.onDestroy()
        mSocket.emit("left", gson.toJson(RoomData(username, roomNumber)))
        mSocket.disconnect()
    }
}