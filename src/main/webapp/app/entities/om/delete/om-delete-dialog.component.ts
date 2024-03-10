import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOM } from '../om.model';
import { OMService } from '../service/om.service';

@Component({
  standalone: true,
  templateUrl: './om-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OMDeleteDialogComponent {
  oM?: IOM;

  constructor(
    protected oMService: OMService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.oMService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
