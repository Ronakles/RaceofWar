# Race System Test Guide

## Test Steps:

1. **Oyunu Başlat**
   - Android Studio'da projeyi aç
   - Emulator veya cihazda çalıştır

2. **Irk Butonlarını Test Et**
   - Alt kısımda 4 ırk butonu görünmeli:
     - ⚙️ Mekanik (Gri)
     - 👑 İnsan (Sarı)
     - 🌿 Elf (Yeşil)
     - 💀 Karanlık (Beyaz)

3. **Unit Butonlarını Test Et**
   - **Elf ırkında**: PNG assetleri kullanılmalı (gerçek resimler)
   - **Diğer ırklarda**: Emoji ikonları kullanılmalı:
     - 🔨 Heavy Weapon
     - 🗡️ Spearman
     - 🏹 Archer
     - ⚔️ Knight
     - 🐎 Cavalry
   - **PNG yüklenemezse**: ❓ emoji gösterilmeli (fallback)

4. **Irk Değişikliğini Test Et**
   - Farklı ırk butonlarına tıkla
   - Seçili ırk altın renkte vurgulanmalı
   - Unit butonlarının görünümü değişmeli (PNG ↔ Emoji)
   - Birim spawn et

5. **Birim Renklerini Kontrol Et**
   - **Mekanik**: Koyu gri birimler
   - **İnsan**: Sarı/altın renkli birimler
   - **Elf**: Yeşil birimler
   - **Karanlık**: Beyaz birimler

6. **Beklenen Sonuçlar**
   - Her ırk değişikliğinde yeni spawn edilen birimler o ırkın renginde olmalı
   - **Mevcut birimlerin rengi de anında değişmeli** (yeni özellik!)
   - Tüm birim türleri (Cavalry, Spearman, Archer, Knight, Heavy Weapon) seçilen ırkın renginde olmalı
   - Irk değişikliği anında tüm ekrandaki oyuncu birimlerine uygulanmalı
   - **Elf ırkında PNG assetleri, diğer ırklarda emoji ikonları görünmeli**

## Hata Durumları:
- Eğer birimler hala aynı renkte geliyorsa, SpawnSystem'deki selectedRace parametresi doğru geçirilmiyor olabilir
- Eğer ırk butonları görünmüyorsa, RaceButtons komponenti doğru import edilmemiş olabilir
- Eğer renkler yanlışsa, GameConfig'deki ırk renkleri yanlış olabilir
