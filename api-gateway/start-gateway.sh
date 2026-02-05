#!/bin/bash

echo "=========================================="
echo "INICIANDO API GATEWAY - Relatos de Papel"
echo "=========================================="

cd /home/diego_arevalo/Escritorio/backend-unir/api-gateway

echo ""
echo "✅ Compilando proyecto..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Error en la compilación"
    exit 1
fi

echo ""
echo "✅ Iniciando API Gateway en puerto 8080..."
java -jar target/api-gateway-1.0.0.jar &

PID=$!
echo "Process ID: $PID"

echo ""
echo "⏳ Esperando a que el servicio inicie (30 segundos)..."
sleep 30

echo ""
echo "🔍 Verificando estado del servicio..."
HEALTH=$(curl -s http://localhost:8080/actuator/health | grep -o '"status":"UP"' | head -1)

if [ -n "$HEALTH" ]; then
    echo "✅ API Gateway está corriendo correctamente!"
    echo ""
    echo "📋 Información del servicio:"
    echo "   - Puerto: 8080"
    echo "   - Health Check: http://localhost:8080/actuator/health"
    echo "   - API Endpoint: POST http://localhost:8080/api/gateway"
    echo "   - PID: $PID"
else
    echo "⚠️  El servicio puede no estar completamente iniciado todavía."
    echo "   Verifica los logs en: target/*.log"
fi

echo ""
echo "=========================================="
