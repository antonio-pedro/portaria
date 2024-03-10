import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPostoGraduacao } from '../posto-graduacao.model';
import { PostoGraduacaoService } from '../service/posto-graduacao.service';

@Component({
  standalone: true,
  templateUrl: './posto-graduacao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PostoGraduacaoDeleteDialogComponent {
  postoGraduacao?: IPostoGraduacao;

  constructor(
    protected postoGraduacaoService: PostoGraduacaoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.postoGraduacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
