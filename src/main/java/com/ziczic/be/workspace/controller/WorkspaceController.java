package com.ziczic.be.workspace.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ziczic.be.global.jwt.JwtUtil;
import com.ziczic.be.workspace.dto.WorkspaceCreateReq;
import com.ziczic.be.workspace.dto.WorkspaceResp;
import com.ziczic.be.workspace.entity.Workspace;
import com.ziczic.be.workspace.serivce.WorkspaceService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

	private final WorkspaceService workspaceService;
	private final JwtUtil jwtUtil;
	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public void createWorkspace(@RequestBody WorkspaceCreateReq req) {
		workspaceService.createWorkspace(req.accessToken(), req.name());
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<WorkspaceResp>> getWorkspaceList(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);

		log.info("jwtToken = {}", jwtToken);

		Claims claims = jwtUtil.getClaimsFromToken(jwtToken);
		log.info("claims : {}", claims);

		Long memberId = claims.get("id", Long.class);
		log.info("memberId = {}", memberId);

		List<WorkspaceResp> workspaces = workspaceService.getWorkspaceList(memberId);

		// log.info("workspace name : {}", workspaces.get(0).getWorkspaceName());

		return ResponseEntity.ok(workspaces);
	}




}
