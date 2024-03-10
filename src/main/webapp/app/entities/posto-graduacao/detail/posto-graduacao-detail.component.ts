import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPostoGraduacao } from '../posto-graduacao.model';

@Component({
  standalone: true,
  selector: 'jhi-posto-graduacao-detail',
  templateUrl: './posto-graduacao-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PostoGraduacaoDetailComponent {
  @Input() postoGraduacao: IPostoGraduacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
