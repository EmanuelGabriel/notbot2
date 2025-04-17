package br.com.emanuelgabriel.notbot2.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Emanuel Gabriel
 * Classe responsável por armazenar e carregar IDs de vídeos em um arquivo JSON.
 */
public class FileStorage {

    private static final Logger logger = Logger.getLogger(FileStorage.class.getName());
    private static final String IDS_FILE_PATH = "notificaVideos.json";
    private static final Gson gson = new Gson();

    /**
     * Carrega os IDs do arquivo JSON.
     *
     * @return Um conjunto de IDs carregados do arquivo ou um conjunto vazio se
     * o arquivo não existir.
     */
    public static Set<String> carregarIds() {
        try (Reader reader = Files.newBufferedReader(Paths.get(IDS_FILE_PATH))) {
            return gson.fromJson(reader, new TypeToken<Set<String>>() {
            }.getType());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao carregar IDs do arquivo ou arquivo não encontrado. Criando novo conjunto. {0}", e.getMessage());
            return new HashSet<>();
        }
    }

    /**
     * Salva os IDs no arquivo JSON.
     *
     * @param ids O conjunto de IDs a ser salvo.
     */
    public static void salvarIds(Set<String> ids) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(IDS_FILE_PATH))) {
            gson.toJson(ids, writer);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao salvar IDs no arquivo: {0}", e.getMessage());
        }
    }


}

