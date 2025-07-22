<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>GeoLocalizador IP - IFSC</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 40px;
            max-width: 600px;
            width: 100%;
            text-align: center;
        }

        .header {
            margin-bottom: 30px;
        }

        .header h1 {
            color: #333;
            font-size: 2.5rem;
            margin-bottom: 10px;
            font-weight: 700;
        }

        .header .subtitle {
            color: #666;
            font-size: 1.1rem;
            margin-bottom: 5px;
        }

        .header .institute {
            color: #667eea;
            font-weight: 600;
            font-size: 1rem;
        }

        .form-section {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 30px;
            margin: 30px 0;
        }

        .mode-selector {
            margin-bottom: 25px;
        }

        .mode-selector label {
            display: block;
            margin-bottom: 10px;
            color: #333;
            font-weight: 600;
            font-size: 1.1rem;
        }

        select {
            width: 100%;
            padding: 15px;
            border: 2px solid #e1e5e9;
            border-radius: 10px;
            font-size: 1rem;
            background: white;
            transition: all 0.3s ease;
        }

        select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .ip-input-section {
            margin: 25px 0;
            opacity: 0;
            transform: translateY(-10px);
            transition: all 0.3s ease;
        }

        .ip-input-section.show {
            opacity: 1;
            transform: translateY(0);
        }

        .ip-input-section label {
            display: block;
            margin-bottom: 10px;
            color: #333;
            font-weight: 600;
            text-align: left;
        }

        .input-group {
            position: relative;
        }

        .input-group i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #667eea;
            font-size: 1.1rem;
        }

        input[type="text"] {
            width: 100%;
            padding: 15px 15px 15px 45px;
            border: 2px solid #e1e5e9;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        input[type="text"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 15px 40px;
            border-radius: 10px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 20px;
            min-width: 150px;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-primary:active {
            transform: translateY(0);
        }

        .btn-primary:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }

        .result-section {
            margin-top: 30px;
            min-height: 100px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .result-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 15px;
            width: 100%;
            opacity: 0;
            transform: translateY(20px);
            transition: all 0.5s ease;
        }

        .result-card.show {
            opacity: 1;
            transform: translateY(0);
        }

        .result-card h3 {
            margin-bottom: 15px;
            font-size: 1.3rem;
        }

        .result-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            text-align: left;
        }

        .result-item {
            background: rgba(255,255,255,0.1);
            padding: 15px;
            border-radius: 10px;
            backdrop-filter: blur(10px);
        }

        .result-item strong {
            display: block;
            margin-bottom: 5px;
            font-size: 0.9rem;
            opacity: 0.8;
        }

        .result-item span {
            font-size: 1.1rem;
            font-weight: 600;
        }

        .loading {
            display: flex;
            align-items: center;
            justify-content: center;
            color: #667eea;
            font-size: 1.1rem;
        }

        .loading i {
            margin-right: 10px;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .error {
            background: #ff6b6b;
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }

        .success {
            background: #51cf66;
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }

        .examples {
            margin-top: 20px;
            text-align: left;
        }

        .examples h4 {
            color: #333;
            margin-bottom: 10px;
            font-size: 1rem;
        }

        .example-ips {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .example-ip {
            background: #e9ecef;
            color: #495057;
            padding: 8px 12px;
            border-radius: 20px;
            font-size: 0.9rem;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .example-ip:hover {
            background: #667eea;
            color: white;
            transform: translateY(-1px);
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
                margin: 10px;
            }

            .header h1 {
                font-size: 2rem;
            }

            .result-info {
                grid-template-columns: 1fr;
            }

            .example-ips {
                justify-content: center;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1><i class="fas fa-globe-americas"></i> GeoLocalizador IP</h1>
            <div class="subtitle">Descubra a localização geográfica de qualquer endereço IP</div>
            <div class="institute">Instituto Federal de Santa Catarina - IFSC</div>
        </div>

        <div class="form-section">
            <div class="mode-selector">
                <label for="modo"><i class="fas fa-cog"></i> Modo de Consulta:</label>
                <select id="modo" onchange="alternarModo()">
                    <option value="meu">🔍 Detectar meu IP automaticamente</option>
                    <option value="outro">✏️ Informar outro endereço IP</option>
                </select>
            </div>

            <div id="campo-ip" class="ip-input-section">
                <label for="ip"><i class="fas fa-network-wired"></i> Endereço IP:</label>
                <div class="input-group">
                    <i class="fas fa-map-marker-alt"></i>
                    <input type="text" id="ip" placeholder="Ex: 8.8.8.8, 1.1.1.1, 208.67.222.222" />
                </div>
                
                <div class="examples">
                    <h4>Exemplos para testar:</h4>
                    <div class="example-ips">
                        <span class="example-ip" onclick="setExampleIP('8.8.8.8')">8.8.8.8</span>
                        <span class="example-ip" onclick="setExampleIP('1.1.1.1')">1.1.1.1</span>
                        <span class="example-ip" onclick="setExampleIP('208.67.222.222')">208.67.222.222</span>
                        <span class="example-ip" onclick="setExampleIP('4.4.4.4')">4.4.4.4</span>
                    </div>
                </div>
            </div>

            <button class="btn-primary" onclick="consultar()" id="btnConsultar">
                <i class="fas fa-search"></i> Consultar Localização
            </button>
        </div>

        <div class="result-section" id="resultado">
            <!-- Resultado será inserido aqui -->
        </div>
    </div>
<script>
    let isLoading = false;

    function alternarModo() {
        const modo = document.getElementById('modo').value;
        const campoIP = document.getElementById('campo-ip');
        limparResultado();
        
        if (modo === 'outro') {
            campoIP.classList.add('show');
        } else {
            campoIP.classList.remove('show');
        }
    }

    function setExampleIP(ip) {
        document.getElementById('ip').value = ip;
        document.getElementById('modo').value = 'outro';
        alternarModo();
    }

    function limparResultado() {
        const resultado = document.getElementById('resultado');
        resultado.innerHTML = '';
    }

    function mostrarLoading() {
        const resultado = document.getElementById('resultado');
        resultado.innerHTML = `
            <div class="loading">
                <i class="fas fa-spinner"></i>
                Consultando localização...
            </div>
        `;
        isLoading = true;
        document.getElementById('btnConsultar').disabled = true;
    }

    function esconderLoading() {
        isLoading = false;
        document.getElementById('btnConsultar').disabled = false;
    }

    function mostrarResultado(dados, ip) {
        const resultado = document.getElementById('resultado');
        
        if (!dados || (!dados.pais && !dados.local)) {
            resultado.innerHTML = `
                <div class="error">
                    <i class="fas fa-exclamation-triangle"></i>
                    <strong>Localização não encontrada</strong><br>
                    Não foi possível determinar a localização para o IP: ${ip}
                </div>
            `;
            return;
        }

        const pais = dados.pais || 'N/A';
        const local = dados.local || 'N/A';
        
        resultado.innerHTML = `
            <div class="result-card">
                <h3><i class="fas fa-map-marked-alt"></i> Localização Encontrada</h3>
                <div class="result-info">
                    <div class="result-item">
                        <strong><i class="fas fa-flag"></i> País:</strong>
                        <span>${pais}</span>
                    </div>
                    <div class="result-item">
                        <strong><i class="fas fa-city"></i> Localidade:</strong>
                        <span>${local}</span>
                    </div>
                </div>
            </div>
        `;
        
        // Anima a entrada do resultado
        setTimeout(() => {
            const card = resultado.querySelector('.result-card');
            if (card) card.classList.add('show');
        }, 100);
    }

    function mostrarErro(mensagem) {
        const resultado = document.getElementById('resultado');
        resultado.innerHTML = `
            <div class="error">
                <i class="fas fa-exclamation-circle"></i>
                <strong>Erro na consulta</strong><br>
                ${mensagem}
            </div>
        `;
    }

    async function consultar() {
        if (isLoading) return;
        
        const modo = document.getElementById('modo').value;
        let url = '/geoip';
        let ipConsultado = 'seu IP';

        if (modo === 'outro') {
            const ip = document.getElementById('ip').value.trim();
            if (!ip) {
                resultado.textContent = 'Digite um endereço IP.';
                return;
            }
            url = `/geoip/${ip}`;
        }

        try {
            resultado.textContent = 'Consultando...';
            const response = await fetch(url);
            const dados = await response.json();

            if (!dados.pais && !dados.local) {
                resultado.textContent = 'Dados de localização não disponíveis.';
                return;
            }

            resultado.innerHTML = `
          <p><strong>País:</strong> ${dados.pais || 'N/A'}</p>
          <p><strong>Localidade:</strong> ${dados.local || 'N/A'}</p>
        `;
        } catch (error) {
            resultado.textContent = 'Localidade deste endereço é desconhecida';
            console.error(error);
        }
    }

    alternarModo();
</script>
</body>
</html>
