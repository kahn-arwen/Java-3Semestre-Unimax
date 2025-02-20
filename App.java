import React, { useState } from 'react';
import { View, TextInput, Button, Text, StyleSheet } from 'react-native';

export default function App() {
  // Definindo os estados para as notas, a média, o resultado e a cor
  const [nota1, setNota1] = useState('');
  const [nota2, setNota2] = useState('');
  const [media, setMedia] = useState(null);
  const [resultado, setResultado] = useState('');
  const [cor, setCor] = useState('black');

  // Função para calcular a média e mostrar o resultado
  const calcularMedia = () => {
    // Convertendo as notas para números
    const n1 = parseFloat(nota1);
    const n2 = parseFloat(nota2);

    // Verificando se as notas são números válidos
    if (isNaN(n1) || isNaN(n2)) {
      setResultado('Por favor, insira números válidos');
      setCor('black');
      setMedia(null);
      return;
    }

    // Calculando a média
    const mediaCalculada = (n1 + n2) / 2;
    setMedia(mediaCalculada.toFixed(1)); // Exibindo a média com 1 casa decimal

    // Definindo o resultado e a cor do texto
    if (mediaCalculada >= 6) {
      setResultado('APROVADO');
      setCor('green');  // Cor verde para aprovado
    } else {
      setResultado('REPROVADO');
      setCor('red');    // Cor vermelha para reprovado
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Calculadora de Média</Text>

      {/* Campo para digitar a primeira nota */}
      <TextInput
        style={styles.input}
        placeholder="Digite a primeira nota"
        keyboardType="numeric"
        value={nota1}
        onChangeText={setNota1}
      />

      {/* Campo para digitar a segunda nota */}
      <TextInput
        style={styles.input}
        placeholder="Digite a segunda nota"
        keyboardType="numeric"
        value={nota2}
        onChangeText={setNota2}
      />

      {/* Botão para calcular a média */}
      <Button title="Checar Média" onPress={calcularMedia} />

      {/* Exibindo a média */}
      {media !== null && (
        <Text style={styles.media}>Média: {media}</Text>
      )}

      {/* Resultado da média (APROVADO ou REPROVADO) */}
      <Text style={[styles.resultado, { color: cor }]}>{resultado}</Text>
    </View>
  );
}

// Estilos do aplicativo
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5f5f5',
    padding: 20,
  },
  title: {
    fontSize: 24,
    marginBottom: 20,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 20,
    width: '80%',
    paddingLeft: 10,
    fontSize: 16,
  },
  media: {
    fontSize: 20,
    marginTop: 20,
  },
  resultado: {
    fontSize: 24,
    marginTop: 20,
  },
});
    

