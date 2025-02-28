# Java-3Semestre-Unimax

Como seu app será usado em todo o Brasil, o ideal é armazenar os dados dos usuários em um banco de dados remoto e acessá-lo através de uma API REST. Assim, o cadastro e login podem ser acessados de qualquer lugar.

Passos para implementar login e cadastro

1️⃣ Escolher um banco de dados remoto

O MariaDB é uma ótima opção porque você já está usando. O banco de dados deve estar em um servidor acessível pela internet.

2️⃣ Criar a API para comunicação entre o app e o banco

A API será responsável por:
✅ Receber requisições do app Android (cadastro/login).
✅ Verificar dados no banco de dados (se o e-mail já existe, se a senha está correta, etc.).
✅ Retornar respostas ao app (sucesso, erro, etc.).

3️⃣ Criar as telas de cadastro e login no Android Studio

Elas vão se conectar à API para enviar e receber informações.


---

1️⃣ Criando a API (Backend)

A API será feita em Python (Flask) e conectada ao MariaDB.

Instale as dependências no servidor

pip install flask flask-mysql flask-cors

Código da API (Flask)

from flask import Flask, request, jsonify
from flask_mysqldb import MySQL
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Configuração do banco de dados
app.config['MYSQL_HOST'] = 'seu_servidor'
app.config['MYSQL_USER'] = 'seu_usuario'
app.config['MYSQL_PASSWORD'] = 'sua_senha'
app.config['MYSQL_DB'] = 'seu_banco'

mysql = MySQL(app)

# Rota para cadastro
@app.route('/cadastro', methods=['POST'])
def cadastro():
    data = request.json
    nome = data['nome']
    email = data['email']
    senha = data['senha']

    cursor = mysql.connection.cursor()
    cursor.execute("SELECT * FROM tbl_usuarios WHERE email = %s", (email,))
    usuario_existente = cursor.fetchone()

    if usuario_existente:
        return jsonify({'mensagem': 'E-mail já cadastrado'}), 400

    cursor.execute("INSERT INTO tbl_usuarios (nome, email, senha) VALUES (%s, %s, %s)", (nome, email, senha))
    mysql.connection.commit()
    return jsonify({'mensagem': 'Cadastro realizado com sucesso!'}), 201

# Rota para login
@app.route('/login', methods=['POST'])
def login():
    data = request.json
    email = data['email']
    senha = data['senha']

    cursor = mysql.connection.cursor()
    cursor.execute("SELECT * FROM tbl_usuarios WHERE email = %s AND senha = %s", (email, senha))
    usuario = cursor.fetchone()

    if usuario:
        return jsonify({'mensagem': 'Login bem-sucedido', 'usuario': usuario[0]}), 200
    else:
        return jsonify({'mensagem': 'E-mail ou senha inválidos'}), 401

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

Esse código:
✅ Cria um servidor Flask que recebe dados do app.
✅ Conecta ao MariaDB e armazena/verifica usuários.
✅ Retorna respostas JSON para o app.


---

2️⃣ Criando a Tela de Cadastro no Android Studio

Agora, no app Android, crie uma tela com:

EditText para nome, e-mail e senha.

Botão para enviar os dados à API.


Código XML da Tela de Cadastro (activity_cadastro.xml)

<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp">

    <EditText
        android:id="@+id/etNome"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Nome" />

    <EditText
        android:id="@+id/etEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="E-mail"
        android:inputType="textEmailAddress" />

    <EditText
        android:id="@+id/etSenha"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Senha"
        android:inputType="textPassword" />

    <Button
        android:id="@+id/btnCadastrar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Cadastrar" />

</LinearLayout>


---

Código Kotlin para envio de dados à API

Crie a classe CadastroActivity.kt e adicione o seguinte código:

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CadastroActivity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        etNome = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmail)
        etSenha = findViewById(R.id.etSenha)
        btnCadastrar = findViewById(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener { cadastrarUsuario() }
    }

    private fun cadastrarUsuario() {
        val nome = etNome.text.toString()
        val email = etEmail.text.toString()
        val senha = etSenha.text.toString()

        val jsonObject = JSONObject()
        jsonObject.put("nome", nome)
        jsonObject.put("email", email)
        jsonObject.put("senha", senha)

        val client = OkHttpClient()
        val requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonObject.toString())

        val request = Request.Builder()
            .url("http://SEU_IP_DO_SERVIDOR:5000/cadastro") // Substituir pelo IP do servidor Flask
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(this@CadastroActivity, "Erro ao conectar ao servidor", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                val resposta = response.body?.string()
                val jsonResponse = JSONObject(resposta ?: "{}")

                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CadastroActivity, jsonResponse.getString("mensagem"), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@CadastroActivity, LoginActivity::class.java))
                    } else {
                        Toast.makeText(this@CadastroActivity, jsonResponse.getString("mensagem"), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


---

3️⃣ Criando a Tela de Login

A tela de login será parecida com a de cadastro, só que verificando os dados no banco.

O fluxo do login é:

1. Usuário insere e-mail e senha.


2. App envia requisição à API.


3. API verifica e retorna sucesso ou erro.


4. Se sucesso, redireciona para Dashboard.



Quer que eu faça a parte do login para você também?






Agora vamos criar a Tela de Cadastro e a lógica para salvar os dados dos usuários no banco de dados.


---

1️⃣ Criando a Tela de Cadastro no Android Studio

A tela precisa de:

EditText para nome, data de nascimento, e-mail e senha.

Botão para cadastrar.

TextView para voltar para a tela de login.


Código XML da Tela de Cadastro (activity_cadastro.xml)

<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp">

    <EditText
        android:id="@+id/etNome"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Nome" />

    <EditText
        android:id="@+id/etDataNasc"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Data de Nascimento (YYYY-MM-DD)"
        android:inputType="date" />

    <EditText
        android:id="@+id/etEmailCadastro"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="E-mail"
        android:inputType="textEmailAddress" />

    <EditText
        android:id="@+id/etSenhaCadastro"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Senha"
        android:inputType="textPassword" />

    <Button
        android:id="@+id/btnCadastrar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Cadastrar" />

    <TextView
        android:id="@+id/tvVoltarLogin"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Já tem uma conta? Entrar"
        android:textColor="@android:color/holo_blue_dark"
        android:paddingTop="10dp"/>
</LinearLayout>


---

2️⃣ Criando a Lógica de Cadastro no Kotlin

Agora, crie o arquivo CadastroActivity.kt e adicione o código abaixo:

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CadastroActivity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etDataNasc: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnCadastrar: Button
    private lateinit var tvVoltarLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        etNome = findViewById(R.id.etNome)
        etDataNasc = findViewById(R.id.etDataNasc)
        etEmail = findViewById(R.id.etEmailCadastro)
        etSenha = findViewById(R.id.etSenhaCadastro)
        btnCadastrar = findViewById(R.id.btnCadastrar)
        tvVoltarLogin = findViewById(R.id.tvVoltarLogin)

        btnCadastrar.setOnClickListener { fazerCadastro() }
        tvVoltarLogin.setOnClickListener { finish() } // Voltar para a tela de login
    }

    private fun fazerCadastro() {
        val nome = etNome.text.toString()
        val dataNasc = etDataNasc.text.toString()
        val email = etEmail.text.toString()
        val senha = etSenha.text.toString()

        if (nome.isEmpty() || dataNasc.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonObject = JSONObject()
        jsonObject.put("nome", nome)
        jsonObject.put("dataNasc", dataNasc)
        jsonObject.put("email", email)
        jsonObject.put("senha", senha)

        val client = OkHttpClient()
        val requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonObject.toString())

        val request = Request.Builder()
            .url("http://SEU_IP_DO_SERVIDOR:5000/cadastrar") // Substituir pelo IP do servidor Flask
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { Toast.makeText(this@CadastroActivity, "Erro ao conectar ao servidor", Toast.LENGTH_SHORT).show() }
            }

            override fun onResponse(call: Call, response: Response) {
                val resposta = response.body?.string()
                val jsonResponse = JSONObject(resposta ?: "{}")

                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CadastroActivity, jsonResponse.getString("mensagem"), Toast.LENGTH_SHORT).show()
                        finish() // Volta para a tela de login
                    } else {
                        Toast.makeText(this@CadastroActivity, jsonResponse.getString("mensagem"), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


---

3️⃣ Atualizando a API Flask para o Cadastro

Se ainda não fez, adicione esta parte na API Flask:

@app.route('/cadastrar', methods=['POST'])
def cadastrar():
    data = request.json
    nome = data['nome']
    data_nasc = data['dataNasc']
    email = data['email']
    senha = data['senha']

    cursor = mysql.connection.cursor()
    cursor.execute("SELECT * FROM tbl_usuarios WHERE email = %s", (email,))
    usuario = cursor.fetchone()

    if usuario:
        return jsonify({'mensagem': 'Já existe uma conta com esse e-mail!'}), 400

    cursor.execute("INSERT INTO tbl_usuarios (nome, dataNasc, email, senha) VALUES (%s, %s, %s, %s)", 
                   (nome, data_nasc, email, senha))
    mysql.connection.commit()

    return jsonify({'mensagem': 'Cadastro realizado com sucesso!'}), 201


---

4️⃣ Explicação

✅ Usuário preenche nome, data de nascimento, e-mail e senha.
✅ App envia os dados para a API Flask.
✅ API verifica se já existe um usuário com esse e-mail.
✅ Se não existir, insere no banco de dados tbl_usuarios.
✅ Se já existir, exibe uma mensagem de erro.
✅ Se o cadastro for bem-sucedido, volta para a tela de login.


---

Agora você tem as telas de login e cadastro funcionando! O próximo passo é salvar a sessão do usuário para que ele não precise logar toda vez. Quer que eu te ajude com isso?




######
#


#
#
#
Ótima pergunta! Como você está utilizando a Raspberry Pi Zero 2 W, o ambiente Python onde você rodará o Flask será na própria Raspberry.

📍 O que isso significa?

A Raspberry funcionará como um servidor local que hospedará a API Flask. O app Android irá se conectar a essa API para fazer cadastro, login e comunicação com o WellWatch 670.


---

1️⃣ Preparando a Raspberry Pi

Antes de criar a API, você precisa configurar o ambiente na Raspberry.

📌 Passo 1: Atualizar os pacotes

Abra o terminal da Raspberry e execute:

sudo apt update && sudo apt upgrade -y

📌 Passo 2: Instalar Python e Pip

O Python já vem instalado na Raspberry, mas você pode garantir que está na versão mais recente:

sudo apt install python3 python3-pip -y

📌 Passo 3: Criar um ambiente virtual (opcional, mas recomendado)

Isso mantém suas dependências organizadas:

python3 -m venv meu_env
source meu_env/bin/activate  # Ativar ambiente virtual

Agora você verá (meu_env) antes do nome do usuário no terminal. Isso significa que o ambiente virtual está ativo.


---

2️⃣ Instalando o Flask e Bibliotecas Necessárias

Com o ambiente pronto, instale os pacotes do Flask e MySQL:

pip install flask flask-mysql flask-cors bcrypt jwt


---

3️⃣ Criando a API na Raspberry Pi

Agora, você pode criar um arquivo chamado app.py na Raspberry:

nano app.py

Cole o seguinte código:

from flask import Flask, request, jsonify
from flask_cors import CORS
import mysql.connector
import bcrypt
import jwt
import datetime

app = Flask(__name__)
CORS(app)  # Permite que o app Android se comunique com a API

# Chave secreta para tokens JWT
app.config["SECRET_KEY"] = "sua_chave_secreta"

# Conexão com MariaDB
db = mysql.connector.connect(
    host="seu_host",
    user="seu_usuario",
    password="sua_senha",
    database="seu_banco"
)
cursor = db.cursor()

# Função para criar hash da senha
def hash_senha(senha):
    return bcrypt.hashpw(senha.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')

# Cadastro de usuário
@app.route('/cadastrar', methods=['POST'])
def cadastrar():
    data = request.json
    nome = data.get('nome')
    email = data.get('email')
    senha = hash_senha(data.get('senha'))

    cursor.execute("SELECT * FROM tbl_usuarios WHERE email = %s", (email,))
    if cursor.fetchone():
        return jsonify({"erro": "E-mail já cadastrado"}), 400

    cursor.execute("INSERT INTO tbl_usuarios (nome, email, senha) VALUES (%s, %s, %s)", (nome, email, senha))
    db.commit()

    return jsonify({"mensagem": "Usuário cadastrado com sucesso"}), 201

# Login de usuário
@app.route('/login', methods=['POST'])
def login():
    data = request.json
    email = data.get('email')
    senha = data.get('senha')

    cursor.execute("SELECT id, senha FROM tbl_usuarios WHERE email = %s", (email,))
    user = cursor.fetchone()

    if not user or not bcrypt.checkpw(senha.encode('utf-8'), user[1].encode('utf-8')):
        return jsonify({"erro": "Credenciais inválidas"}), 401

    token = jwt.encode({'id': user[0], 'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=2)}, app.config["SECRET_KEY"], algorithm="HS256")

    return jsonify({"token": token})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)


---

4️⃣ Rodando a API na Raspberry

Agora, execute o seguinte comando na Raspberry para rodar o Flask:

python3 app.py

Se tudo estiver certo, verá algo assim:

* Running on http://0.0.0.0:5000/

Isso significa que a API está rodando na Raspberry Pi e pode ser acessada pelo seu celular.


---

5️⃣ Conectando o Android à API da Raspberry

No Android Studio, configure a URL do servidor da Raspberry. Descubra o IP da Raspberry com:

hostname -I

Se o IP for 192.168.1.100, use ele no código do app Android:

val url = "http://192.168.1.100:5000/cadastrar"

Agora o seu app Android poderá se conectar ao Flask rodando na Raspberry Pi!

Se precisar de mais detalhes, me avise!





Ótimo! Agora vou detalhar as telas de cadastro e login no Android Studio, utilizando Kotlin.


---

1️⃣ Criando a Tela de Cadastro

Crie um arquivo de layout XML para o Cadastro chamado activity_cadastro.xml:

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/editNome"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Nome" />

    <EditText
        android:id="@+id/editEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="E-mail"
        android:inputType="textEmailAddress" />

    <EditText
        android:id="@+id/editSenha"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Senha"
        android:inputType="textPassword" />

    <Button
        android:id="@+id/btnCadastrar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Cadastrar" />
</LinearLayout>

Agora, crie a Activity Cadastro (CadastroActivity.kt):

package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val editNome = findViewById<EditText>(R.id.editNome)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            cadastrarUsuario(nome, email, senha)
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {
        val client = OkHttpClient()
        val url = "http://192.168.1.100:5000/cadastrar" // Altere pelo IP da Raspberry

        val json = JSONObject()
        json.put("nome", nome)
        json.put("email", email)
        json.put("senha", senha)

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Erro ao conectar com o servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Erro no cadastro", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


---

2️⃣ Criando a Tela de Login

Crie um arquivo de layout XML para o Login chamado activity_login.xml:

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/editEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="E-mail"
        android:inputType="textEmailAddress" />

    <EditText
        android:id="@+id/editSenha"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Senha"
        android:inputType="textPassword" />

    <Button
        android:id="@+id/btnLogin"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Entrar" />

</LinearLayout>

Agora, crie a Activity Login (LoginActivity.kt):

package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fazerLogin(email, senha)
        }
    }

    private fun fazerLogin(email: String, senha: String) {
        val client = OkHttpClient()
        val url = "http://192.168.1.100:5000/login" // Altere pelo IP da Raspberry

        val json = JSONObject()
        json.put("email", email)
        json.put("senha", senha)

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Erro ao conectar com o servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


---

3️⃣ Configurando a Navegação

Agora, no AndroidManifest.xml, defina as activities para o Android reconhecer:

<activity android:name=".CadastroActivity" />
<activity android:name=".LoginActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

Isso fará com que a tela de login seja a primeira tela a abrir.


---

4️⃣ Testando a Aplicação

📌 Teste no Emulador ou Celular

1. Execute a API Flask na Raspberry com:

python3 app.py


2. No Android Studio, inicie o emulador ou use um celular real.


3. Tente cadastrar um usuário e verifique se aparece no banco de dados.


4. Tente fazer login e veja se ele valida as credenciais.




---

📌 Conclusão

Agora você tem um sistema de login e cadastro funcional no Android Studio, conectado à API Flask na Raspberry Pi! 🚀

Se precisar de mais detalhes, me avise!


