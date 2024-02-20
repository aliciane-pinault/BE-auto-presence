package fr.isen.perigot.educscan.ui.dashboard;

public class Intervention {
        private String id_apprenant;
        private String id_absent;

        public Intervention(String id_apprenant, String id_absent) {
                this.id_apprenant = id_apprenant;
                this.id_absent = id_absent;
        }

        public String getId_apprenant() {
                return id_apprenant;
        }

        public void setId_apprenant(String id_apprenant) {
                this.id_apprenant = id_apprenant;
        }

        public String getId_absent() {
                return id_absent;
        }

        public void setId_absent(String id_absent) {
                this.id_absent = id_absent;
        }
}