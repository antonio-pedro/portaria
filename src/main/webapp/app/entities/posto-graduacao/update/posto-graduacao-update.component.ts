import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPostoGraduacao } from '../posto-graduacao.model';
import { PostoGraduacaoService } from '../service/posto-graduacao.service';
import { PostoGraduacaoFormService, PostoGraduacaoFormGroup } from './posto-graduacao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-posto-graduacao-update',
  templateUrl: './posto-graduacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PostoGraduacaoUpdateComponent implements OnInit {
  isSaving = false;
  postoGraduacao: IPostoGraduacao | null = null;

  editForm: PostoGraduacaoFormGroup = this.postoGraduacaoFormService.createPostoGraduacaoFormGroup();

  constructor(
    protected postoGraduacaoService: PostoGraduacaoService,
    protected postoGraduacaoFormService: PostoGraduacaoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ postoGraduacao }) => {
      this.postoGraduacao = postoGraduacao;
      if (postoGraduacao) {
        this.updateForm(postoGraduacao);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const postoGraduacao = this.postoGraduacaoFormService.getPostoGraduacao(this.editForm);
    if (postoGraduacao.id !== null) {
      this.subscribeToSaveResponse(this.postoGraduacaoService.update(postoGraduacao));
    } else {
      this.subscribeToSaveResponse(this.postoGraduacaoService.create(postoGraduacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPostoGraduacao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(postoGraduacao: IPostoGraduacao): void {
    this.postoGraduacao = postoGraduacao;
    this.postoGraduacaoFormService.resetForm(this.editForm, postoGraduacao);
  }
}
